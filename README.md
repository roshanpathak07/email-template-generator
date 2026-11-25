# Email Template Generator Service

A Spring Boot microservice that generates short, customer-friendly email
templates from a few input fields, using the Google Gemini API.

## Tech stack

- Java 21, Spring Boot 4.x, Maven
- Spring Web and Validation (no database, no extra libraries)
- Google Gemini API, called over plain REST with Spring's `RestClient`

## Setup

1. Get a Gemini API key from Google AI Studio.
2. Set it as an environment variable. Never put it in a file in the repo.

   PowerShell:
   ```powershell
   $env:AI_API_KEY="your-key"
   ```
   macOS / Linux:
   ```bash
   export AI_API_KEY="your-key"
   ```
3. Run the app from the same terminal:
   ```bash
   ./mvnw spring-boot:run
   ```
   On Windows: `.\mvnw.cmd spring-boot:run`

The app reads the key from `AI_API_KEY` and fails at startup if it is missing.
The model name and base URL are in `src/main/resources/application.properties`
(`ai.model`, `ai.base-url`) because model names get retired and should not
live in code. If you hit a 503 "high demand" error, retry or switch `ai.model`
to another stable Flash model.

## API

`POST /api/v1/emails/generate`

Request:
```json
{
  "purpose": "Follow up on an unpaid invoice",
  "recipient_name": "Priya",
  "tone": "friendly"
}
```

Success response (200):
```json
{
  "subject": "Friendly reminder regarding invoice [Invoice Number]",
  "body": "Hi Priya,\n\nI hope you are having a great week!\n\nThis is a quick reminder regarding invoice [Invoice Number] for [Amount], which was due on [Due Date].\n\nPlease let me know if you have any questions.\n\nWarm regards,\n\n[Your Name]"
}
```

Validation error (400), for example when fields are missing:
```json
{
  "status": 400,
  "message": "Validation failed",
  "details": ["recipient_name is required", "tone is required", "purpose is required"],
  "timestamp": "..."
}
```

AI failure (502), when Gemini is down, overloaded or rejects the call:
```json
{
  "status": 502,
  "message": "The AI service is unavailable. Please try again later.",
  "details": [],
  "timestamp": "..."
}
```

## Project structure

| Package | Responsibility |
|---|---|
| `controller` | HTTP only: receives the request, returns the response |
| `service` | Business logic: builds the prompt, calls the client, parses the result |
| `prompt` | The prompt text, in one place |
| `client` | The only class that talks to Gemini |
| `config` | Reads `ai.*` settings, including the key from the environment |
| `exception` | Custom exception and the global error handler |
| `dto` | Request and response objects |

## How the prompt was designed

The prompt lives in `EmailPromptBuilder` so it can be tuned without touching
the controller, service or client.

- **Role and rules first.** The model is told it writes short customer-friendly
  templates, with a 120-word cap and a requirement to match the requested tone
  and address the recipient by name.
- **Placeholders, not inventions.** Unknown details such as `[Invoice Number]`
  must be left as bracketed placeholders, which is what makes the output a
  reusable template rather than made-up facts.
- **Strict output format.** The model must answer as `SUBJECT:` and `BODY:`.
  The service splits on those markers, so no JSON library is needed. If the
  format is wrong, the service returns a 502 instead of passing along garbage.
- **User input is data.** The prompt states that the input values must never be
  treated as instructions, as a basic prompt-injection defense.

## Response-time logging

Each Gemini call is timed in `GeminiClient` with `System.nanoTime()`, logged in
a `finally` block so failures are timed too. Only the duration and model name
are logged, never the prompt, the email text or the key.

```
Gemini call finished in 5480 ms (model=gemini-3.6-flash)
```

## Security notes

- The API key is read from the `AI_API_KEY` environment variable and is never
  logged or committed. `.gitignore` also excludes `.env` and
  `application-local.*` files.
- The key is sent in the `x-goog-api-key` header rather than the URL, so it does
  not end up in URL logs.
