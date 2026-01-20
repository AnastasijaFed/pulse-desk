import "./TicketList.css";

export default function TicketList({ tickets, loading, error }) {
  return (
    <div>
      <h2 className="h2">Support Tickets</h2>

      {loading && <p className="small">Loading…</p>}
      {error && <p className="error">{error}</p>}

      {!loading && !error && tickets.length === 0 && (
        <p className="small">No tickets created yet.</p>
      )}

      {!loading && !error && tickets.length > 0 && (
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Category</th>
              <th>Priority</th>
              <th>Summary</th>
              <th>Created</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((t) => (
              <tr key={t.id}>
                <td><strong>TK-{t.id}</strong></td>
                <td>{t.title}</td>
                <td>{t.category}</td>
                <td>{t.priority}</td>
                <td className="summaryCell">{t.summary}</td>
                <td className="timeCell">
                  {t.createdAt ? new Date(t.createdAt).toLocaleString() : ""}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
