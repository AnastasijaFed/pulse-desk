function TicketInfo({ ticket }) {
  return (
    <tr>
      <td>{ticket.title}</td>
      <td>{ticket.category}</td>
      <td>{ticket.priority}</td>
      <td>{ticket.summary}</td>
    </tr>
  );
}

export default TicketInfo;
