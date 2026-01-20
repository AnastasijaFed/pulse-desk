import { useState, useEffect, useMemo } from "react";
import CommentSubmitForm from "../components/CommentSubmitForm";
import TicketList from "../components/TicketList";
import TicketInfo from "../components/TicketInfo";
import RecentComments from "../components/RecentComments";
import { fetchComments, fetchTickets } from "../api/Api";
import "./HomePage.css"

function HomePage() {
   const [comments, setComments] = useState([]);
  const [tickets, setTickets] = useState([]);

  const [commentsLoading, setCommentsLoading] = useState(true);
  const [ticketsLoading, setTicketsLoading] = useState(true);

  const [commentsError, setCommentsError] = useState(null);
  const [ticketsError, setTicketsError] = useState(null);

  async function loadComments() {
    setCommentsLoading(true);
    setCommentsError(null);
    try {
      const data = await fetchComments();
      setComments(Array.isArray(data) ? data : []);
    } catch (e) {
      setCommentsError(e.message || "Failed to load comments");
    } finally {
      setCommentsLoading(false);
    }
  }

  async function loadTickets() {
    setTicketsLoading(true);
    setTicketsError(null);
    try {
      const data = await fetchTickets();
      setTickets(Array.isArray(data) ? data : []);
    } catch (e) {
      setTicketsError(e.message || "Failed to load tickets");
    } finally {
      setTicketsLoading(false);
    }
  }

  useEffect(() => {
    loadComments();
    loadTickets();
  }, []);

  async function handleSubmitted(newComment) {
    setComments((prev) => [newComment, ...prev]);

    if (newComment?.status === "TICKET_CREATED") {
      await loadTickets();
    }
  }

  const recentComments = useMemo(() => {
    return [...comments]
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      .slice(0, 6);
  }, [comments]);

  return (
    <main className="container">
        
        <div className="grid">
            <div className="comments">
                <CommentSubmitForm onSubmitted={handleSubmitted} />
                <div className="divider" />
                <RecentComments comments={recentComments} loading={commentsLoading} error={commentsError} />

            </div>
            
            <div className="tickets">
                <TicketList tickets={tickets} loading={ticketsLoading} error={ticketsError} />
            </div>

        </div>
        

    

    </main>
      

  );
}

export default HomePage;
