import {EventSource} from "eventsource";
export default async function Page() {
   const eventSource = new EventSource('http://172.22.0.13:8092/api/events');


    fetch('http://172.22.0.11:8090/api/diffbot/product').then(() => {
        eventSource.addEventListener('message', event => {
            console.log(event.data);
        })
    })
    return (
        <div>Hello</div>
    )

}