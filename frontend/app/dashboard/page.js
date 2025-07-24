
export default async function Page() {
    let data = await fetch('http://172.22.0.1:8090/api/scrape/product');
    let posts = await data.json()
    return (
        <ul>
            {posts.map((post) => (
                <li key={post.id}>{post.title}</li>
            ))}
        </ul>
    )
}