import { useState } from "react";
import { submitComment } from "../api/Api";
import "./CommentSubmitForm.css"

function CommentSubmitForm({ onSubmitted, result }) {
  const [content, setContent] = useState("");
  const [userId, setUserId] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const r = await submitComment(content);
      onSubmitted(r);
      setContent("");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="panel">
      
      <div className="panelHeader">
        <h2>Submit Comment</h2>

      </div>

      <form onSubmit={handleSubmit}>
      <div className="field">
        <p>Enter your comment below: </p>

           <textarea className="textarea" rows="4" value={content} onChange={(e) => setContent(e.target.value)} required />
      </div>

      <div className="actions"> 
         <button className="button" disabled={loading}>
        {loading ? "Submitting..." : "Submit Comment"}
      </button>
        </div>

        {error && <div className="error">{error}</div>}
      </form>
    </div>
    
  );
}

export default CommentSubmitForm;
