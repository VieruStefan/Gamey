'use client'
import { useEffect, useState } from 'react';

export function SseComponent() {
    const [latestUpdate, setLatestUpdate] = useState('');
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        let timeoutId;
        const sseUrl = `${process.env.NEXT_PUBLIC_MASTER_API_URL}/aggregator/sse/stream`;
        const eventSource = new EventSource(sseUrl);
        fetch(`/gamedata/api/job/create-job`)
            .then(() => console.log("Initial fetch successful."))
            .catch(e => console.error("Fetch failed: " + e))

        const resetTimeout = () =>
        {
            clearTimeout(timeoutId);

            timeoutId = setTimeout(() =>
            {
                console.log('sse timeout');
                eventSource.close();
                setIsConnected(false);
            }, 10000);
        };

        eventSource.onopen = () => {
            console.log('SSE connection established.');
            setIsConnected(true);
            resetTimeout();
        };

        // Default handler for messages without a specific 'event' name
        eventSource.onmessage = (event) => {
            console.log('Received generic SSE message:', event.data);
            resetTimeout();
        };

        eventSource.addEventListener('product-update', (event) => {
            console.log('Received periodic-update event:', event.data);
            setLatestUpdate(event.data);
            resetTimeout();
        });

        eventSource.addEventListener('heartbeat', (event) => {
            console.log('Received heartbeat event:', event.data);
            resetTimeout();
        });

        eventSource.onerror = (error) => {
            console.error('EventSource failed:', error);
            eventSource.close();
            setIsConnected(false);
            clearTimeout(timeoutId);
        };

        return () => {
            console.log('SSE connection closed.');
            clearTimeout(timeoutId);
            eventSource.close();
        };
    }, []);

    return (
        <div>
            <h1>Server-Sent Events</h1>
            <p>Connection Status: {isConnected? 'Connected' : 'Connected'}</p>
            <p>Latest Update: {latestUpdate}</p>
            <p>{`${process.env.NEXT_PUBLIC_REDUCER_SSE_URL}/api/job/create-job`}</p>
            <p>{`${process.env.NEXT_PUBLIC_MASTER_API_URL}/aggregator/sse/stream`}</p>
        </div>
    );

}