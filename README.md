# External Audit AI Service

## 📌 Project Overview
This project is part of an AI-based External Audit System. The goal is to build intelligent services that can classify user queries, generate responses using LLMs, and enable semantic search using vector databases.

---

## 🚀 Tech Stack
- Python
- Flask
- Groq API
- ChromaDB

---

## 📅 Work Progress

### ✅ Day 1 – Setup
- Project environment setup
- Installed required dependencies
- Configured folder structure

---

### ✅ Day 2 – API Development
- Created Flask application
- Implemented basic API endpoints
- Tested API using Postman

---

### ✅ Day 3 – Groq Integration
- Integrated Groq API for text processing
- Built categorisation API
- Generated structured JSON responses (category, confidence, reasoning)
- Handled API errors and retries

---

### ✅ Day 4 – ChromaDB Integration
- Integrated ChromaDB for vector storage
- Created a collection to store documents
- Converted text into embeddings
- Implemented semantic search using query
- Tested retrieval using `test_chroma.py`

---

## 🧠 Features
- Text classification into categories
- AI-generated responses
- Semantic search using vector database

---

## ▶️ How to Run

```bash
pip install -r requirements.txt
python app.py