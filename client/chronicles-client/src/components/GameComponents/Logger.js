import { ListGroup } from "react-bootstrap";
import { useRef, useEffect } from "react";

export default function Logger({messages}) {

    const messageRef = useRef(null);
  
  
    useEffect(() => {
      // scroll to the bottom of the message list when new messages are added
      messageRef.current.scrollTop = messageRef.current.scrollHeight;
    }, [messages]);
  
    return (
      <div className="message-list" style={{ height: '10vh', overflowY: 'scroll' }} ref={messageRef}>
        <ListGroup variant="flush">
          {messages.map((message, index) => (
            <ListGroup.Item key={index}>{message}</ListGroup.Item>
          ))}
        </ListGroup>
      </div>
    );
}