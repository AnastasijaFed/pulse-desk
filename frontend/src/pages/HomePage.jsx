import { useState } from "react";
import CommentSubmitForm from "../components/CommentSubmitForm";
import TicketList from "../components/TicketList";

function HomePage() {
  const [lastResult, setLastResult] = useState(null);
  const [refreshKey, setRefreshKey] = useState(0);

  function handleCommentSubmitted(result) {
    setLastResult(result);
    if (result.status === "TICKET_CREATED") {
      setRefreshKey((k) => k + 1);
    }
  }

  return (
    <div style={{ padding: "2rem", maxWidth: 800 }}>
      <h1>Pulse Desk</h1>
      <p>AI-powered support triage</p>

      <CommentSubmitForm onSubmitted={handleCommentSubmitted} />

      {lastResult && (
        <p style={{ marginTop: "1rem" }}>
          Status: <strong>{lastResult.status}</strong>
        </p>
      )}

      <TicketList refreshKey={refreshKey} />
    </div>
  );
}

export default HomePage;
