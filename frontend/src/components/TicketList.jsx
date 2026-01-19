import { useEffect, useState } from "react";
import { fetchTickets } from "../api/Api";
import TicketInfo from "./TicketInfo";

function TicketList({ refreshKey }) {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  async function loadTickets() {
    setLoading(true);
    setError(null);

    try {
      const data = await fetchTickets();
      setTickets(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadTickets();
  }, [refreshKey]);

  return (
    <div style={{ marginTop: "2rem" }}>
      <h2>Tickets</h2>

      {loading && <p>Loading tickets...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && tickets.length === 0 && (
        <p>No tickets created yet.</p>
      )}

      {!loading && tickets.length > 0 && (
        <table border="1" cellPadding="8">
          <thead>
            <tr>
              <th>Title</th>
              <th>Category</th>
              <th>Priority</th>
              <th>Summary</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((t) => (
              <TicketInfo key={t.id} ticket={t} />
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default TicketList;
