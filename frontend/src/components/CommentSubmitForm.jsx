import { useState } from "react";
import { submitComment } from "../api/Api";

function CommentSubmitForm({ onSubmitted }) {
  const [content, setContent] = useState("");
  const [userId, setUserId] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const result = await submitComment(content, Number(userId));
      onSubmitted(result);
      setContent("");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Submit Comment</h2>

      <div>
        <label>User ID</label><br />
        <input
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          required
        />
      </div>

      <div style={{ marginTop: "1rem" }}>
        <label>Comment</label><br />
        <textarea
          rows="4"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />
      </div>

      <button style={{ marginTop: "1rem" }} disabled={loading}>
        {loading ? "Submitting..." : "Submit"}
      </button>

      {error && (
        <div style={{ color: "red", marginTop: "0.5rem" }}>
          {error}
        </div>
      )}
    </form>
  );
}

export default CommentSubmitForm;
