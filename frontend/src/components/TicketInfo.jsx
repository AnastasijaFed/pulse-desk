import "./TicketInfo.css";
export default function TicketInfo({ ticket }) {
    if(!ticket){
        return(
            <div>
                <h2 className="h2">Ticket Details</h2>
                <p className="small">Select a ticket from the list to view details.</p>
            </div>
        )
    }
  return (
    <div>
      <h2 className="h2">Ticket Details</h2>
      <div className="detailsGrid">
        <div className="label">Ticket ID</div>
        <div className="value">{ticket.id}</div>

        <div className="label">Title</div>
        <div className="value">{ticket.title}</div>

        <div className="label">Category</div>
        <div className="value">{ticket.category}</div>

        <div className="label">Priority</div>
        <div className="value">{ticket.priority}</div>
      </div>

      <div className="divider" />

      <div className="label" style={{ marginBottom: 8 }}>Description</div>
      <div className="desc">{ticket.summary}</div>
    </div>
  );
}


