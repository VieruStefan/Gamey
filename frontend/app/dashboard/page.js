
export default async function Page() {
    let data = await fetch('http://172.22.0.11:8090/api/diffbot/product');
    let posts = await data.json()
    return (
        <ul>
            {posts.map((post) => (
                <p>{post}</p>
            ))}
        </ul>
    )
}