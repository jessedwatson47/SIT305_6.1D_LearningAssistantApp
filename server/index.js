import express from "express";
import "dotenv/config";

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// OpenAI Init
import OpenAI from "openai";
const client = new OpenAI();

// JSON Schema for structured output
const taskSchema = {
  name: "task_response",
  schema: {
    type: "object",
    properties: {
      tasks: {
        type: "array",
        items: {
          type: "object",
          properties: {
            title: {type: "string"},
            desc: {type: "string"},
            questions: {
              type: "array",
              minItems: 3,
              maxItems: 3,
              items: {
                type: "object",
                properties: {
                  question: {type: "string"},
                  title: {type: "string"},
                  answer1: {type: "string"},
                  answer2: {type: "string"},
                  answer3: {type: "string"},
                  correctanswerid: {type: "number"},
                },
                required: ["question", "title", "answer1", "answer2", "answer3", "correctanswerid"],
                additionalProperties: false,
              },
            },
          },
          required: ["title", "desc", "questions"],
          additionalProperties: false,
        },
      },
    },
    required: ["tasks"],
    additionalProperties: false,
  },
};

// Routes
app.get("/", (req, res) => {
  res.send("Hello Jesse!");
});

app.get("/getExplanation", async (req, res) => {
  const {query} = req.query;
  console.log("QUERY:", req.query);

  const response = await client.responses.create({
    model: "gpt-5.4-nano",
    input: `For the provided quiz data, explain briefly why the selected answer was correct or incorrect.
    Be concise, beginner-friendly, and factual.
    Do not address the user directly.
    Do not use markdown. All text must be plain. If this requires math notation, use programmatic equivalents e.g ^ for powers, * for multiplication etc.
    Query: ${query}`,
  });

  res.send(`${response.output_text}`);
});

app.get("/getTasks", async (req, res) => {
  const {query} = req.query;
  console.log("QUERY:", req.query);

  const response = await client.responses.create({
    model: "gpt-5.4-nano",
    input: `For each topic of interest provided in the query (delimited by commas), generate exactly one task. 
    Each task must contain a short title (e.g. Algorithms: Fundamentals, Data Structures: Basics, Algorithms: Bubble Sort vs Selection Sort), a short description, and 3 quiz questions.
    Each quiz question must contain a question, answer1, answer2, answer3 and the correctanswerid.
    The correctanswerid MUST be 0, 1 or 2.
    Do not address the user. Keep language concise. Do not add fluff or any personality. You are a content generator.
    Return valid JSON only!
    Query: ${query}`,
    text: {
      format: {
        type: "json_schema",
        name: taskSchema.name,
        schema: taskSchema.schema,
      },
    },
  });

  // Extract and Parse
  const result = JSON.parse(response.output_text);

  // Return JSON
  res.json(result);
});

app.get("/getExplanation", async (req, res) => {
  const {query} = req.query;

  const response = await client.responses.create({
    model: "gpt-5.4-nano",
    input: `Write a succinct, concise and slightly guiding hint for the provided query.
    Do not address the user. Do not add fluff. The purpose is for providing simple short hints regarding a quiz.
    Query: ${query}`,
  });

  res.send(`${response.output_text}`);
});

app.get("/testAI", async (req, res) => {
  const response = await client.responses.create({
    model: "gpt-5.4-nano",
    input: "Write a one-sentence bedtime story about a unicorn.",
  });

  res.send(`${response.output_text}`);
});

// Start
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
