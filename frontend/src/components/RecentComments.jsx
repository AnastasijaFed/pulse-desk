import "./RecentComments.css";

function formatTimeAgo(iso) {
  if (!iso) return "";
  const diffMs = Date.now() - new Date(iso).getTime();
  const mins = Math.floor(diffMs / 60000);
  if (mins < 1) return "just now";
  if (mins < 60) return `${mins} min ago`;
  const hours = Math.floor(mins / 60);
  if (hours < 24) return `${hours}h ago`;
  const days = Math.floor(hours / 24);
  return `${days}d ago`;
}

function pillClass(status) {
  if (status === "TICKET_CREATED") return "pill pillTicket";
  if (status === "ANALYSIS_FAILED") return "pill pillFail";
  if (status === "ANALYZED") return "pill pillOk";
  return "pill";
}

function pillText(status) {
  if (status === "TICKET_CREATED") return "Ticket Created";
  if (status === "ANALYSIS_FAILED") return "Analysis Failed";
  if (status === "ANALYZED") return "Analyzed";
  if (status === "RECEIVED") return "Received";
  return status || "";
}

export default function RecentComments({ comments, loading, error }) {
  return (
    <div>
      <h2 className="recentHeader">Recent Comments</h2>

      {loading && <p className="small">Loading…</p>}
      {error && <p className="error">{error}</p>}

      {!loading && !error && comments.length === 0 && (
        <p className="small">No comments yet.</p>
      )}

      {!loading && !error && comments.length > 0 && (
        <ul className="list">
          {comments.map((c) => (
            <li key={c.id} className="item">
              <div className="rowTop">
                <div className="content">{c.content}</div>
                <div className="time">{formatTimeAgo(c.createdAt)}</div>
              </div>
              <div className="rowBottom">
                {c.status && <span className={pillClass(c.status)}>{pillText(c.status)}</span>}
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
