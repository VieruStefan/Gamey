
export default async function Page() {
    let data = await fetch('http://master:8090/api/scrape/product').then(res => res.json()).catch(console.error);
    let posts = await data.json()
    return (
        <ul>
            {posts.map((post) => (
                <li key={post.id}>{post.title}</li>
            ))}
        </ul>
    )
}