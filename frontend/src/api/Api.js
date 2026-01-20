const API_BASE = import.meta.env.VITE_API_BASE;

export async function submitComment(content) {
  const res = await fetch(`${API_BASE}/comments`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ content}),
  });

  if (!res.ok) {
    throw new Error("Failed to submit comment");
  }

  return res.json();
}
export async function fetchComments() {
  const res = await fetch(`${API_BASE}/comments`);
  if (!res.ok) throw new Error("Failed to fetch comments");
  return res.json();
}
export async function fetchTickets() {
  const res = await fetch(`${API_BASE}/tickets`);
  if (!res.ok) {
    throw new Error("Failed to fetch tickets");
  }
  return res.json();
}
