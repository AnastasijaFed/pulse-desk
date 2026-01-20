# PulseDesk 

PulseDesk is a lightweight AI-powered support triage system that analyzes user comments and automatically decides whether they should be converted into support tickets.

The backend is built with Spring Boot (Java) and integrates with the Hugging Face Inference API using the Qwen/Qwen2.5-7B-Instruct model.  
The frontend is built with React (Vite) and provides a clean, single-page interface for submitting comments and viewing tickets in real time.

---

## Features

- Submit user comments via UI
- AI-powered analysis and classification
- Automatic ticket creation for actionable issues
- Ticket categorization (bug, billing, account, feature, other)
- Priority detection (low / medium / high)
- Real-time UI updates (no page refresh)
- Minimal, clean UI design

---

## Architecture Overview
- React (Vite)
- REST API
- Spring Boot (Java)
- Hugging Face Inference API (Qwen/Qwen2.5-7B-Instruct)

---

##  AI Decision Logic

The AI receives a structured prompt and must return strict JSON only, following this schema:

```json
{
  "shouldCreateTicket": boolean,
  "title": string,
  "category": "bug" | "feature" | "billing" | "account" | "other",
  "priority": "low" | "medium" | "high",
  "summary": string
}
```

Rules enforced by prompt:
- Compliments or vague positivity → no ticket
- Clear problem, request for help, billing/account issue → ticket created
- Title max 80 characters
- Summary max 2 sentences

The backend safely extracts and parses the JSON even if the model includes reasoning text.

---

##  Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Web (REST)
- Jackson (JSON parsing)
- Hugging Face Inference API
- Model: Qwen/Qwen2.5-7B-Instruct

### Frontend
- React
- Vite
- Fetch API
- CSS (no UI frameworks)

##  Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Hugging Face account & access token

### Backend Setup
**Environment Variables**
```
export HF_TOKEN=your_huggingface_token
```

**application.yml**
```
huggingface:
  base-url: https://router.huggingface.co/hf-inference/models
  model: Qwen/Qwen2.5-7B-Instruct
  timeout-ms: 10000
```

**Run Backend**
```
./mvnw spring-boot:run
```

Backend will start on:
```
http://localhost:8080
```

### Frontend Setup
```
cd frontend
npm install
```

**Create a .env file:**

```
VITE_API_BASE=http://localhost:8080
```

**Run the frontend:**
```
npm run dev
```
Frontend will be available at:
```
http://localhost:5173
```

---


##  API Endpoints

Submit Comment
```
POST /comments
```

Request body:
```json
{
  "content": "I was charged twice and cannot access my account."
}
```

Get Recent Comments
```
GET /comments
```

Get Tickets`
```
GET /tickets
```

Each ticket includes:
- title
- category
- priority
- summary
- creation timestamp

---

## CORS Configuration

CORS is enabled in the backend for local development:

```
.allowedOrigins("http://localhost:5173")
```

---


## Error Handling

- Backend validates and safely parses AI output
- Malformed AI responses are rejected
- AI responses are logged for debugging
- Frontend handles empty and loading states gracefully
