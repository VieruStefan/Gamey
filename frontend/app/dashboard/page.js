import { useState, useEffect } from "react";

export default async function Page() {
    const [data, setData] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        const eventSource = new EventSource('http://172.22.0.13:8092/api/events');
        fetch('http://172.22.0.11:8090/api/diffbot/product')

        eventSource.onmessage = (event) => {
            setData(event.data);
            setError('');
        };

        eventSource.onerror = (err) => {
            console.error(err);
            setError('Connection to server lost.');
        };

        return () => {
            eventSource.close();
        };
    }, []);

    return (
        <div>
            <div>
                {error ? (
                    <p>{error}</p>
                ) : (
                    <p>
                       <span>{data}</span>
                    </p>
                )}
            </div>
        </div>
    );
}