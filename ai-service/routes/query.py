from flask import Blueprint, request, jsonify
from services.chroma_client import ChromaClient
from services.groq_client import GroqClient

query_bp = Blueprint("query", __name__)

chroma_client = ChromaClient()
groq_client = GroqClient()


@query_bp.route("/query", methods=["POST"])
def query():
    data = request.get_json()

    if not data or "question" not in data:
        return jsonify({"error": "Question is required"}), 400

    question = data.get("question")

    # Retrieve top 3 similar chunks
    sources = chroma_client.query(question, n_results=3)

    # Convert sources into context
    context = "\n".join(sources)

    prompt = f"""
Context:
{context}

Question:
{question}

Answer based only on the given context.
"""

    # Generate answer from Groq
    answer = groq_client.generate_response(prompt)

    return jsonify({
        "answer": answer,
        "sources": sources
    })