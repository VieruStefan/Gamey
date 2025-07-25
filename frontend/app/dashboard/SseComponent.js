// In a Next.js/React component
'use client'
import { useEffect, useState } from 'react';

export default function SseComponent() {
    const [latestUpdate, setLatestUpdate] = useState('');
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        const eventSource = new EventSource('http://localhost:8090/sse/stream');

        eventSource.onopen = () => {
            console.log('SSE connection established.');
            setIsConnected(true);
        };

        // Default handler for messages without a specific 'event' name
        eventSource.onmessage = (event) => {
            console.log('Received generic SSE message:', event.data);
        };

        eventSource.addEventListener('periodic-update', (event) => {
            console.log('Received periodic-update event:', event.data);
            setLatestUpdate(event.data);
        });

        eventSource.onerror = (error) => {
            console.error('EventSource failed:', error);
            eventSource.close();
            setIsConnected(false);
        };

        // Cleanup function to close the connection when the component unmounts
        return () => {
            console.log('SSE connection closed.');
            eventSource.close();
        };
    }, []);

    return (
        <div>
            <h1>Server-Sent Events</h1>
            <p>Connection Status: {isConnected? 'Connected' : 'Connected'}</p>
            <p>Latest Update: {latestUpdate}</p>
        </div>
    );
}