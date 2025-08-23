/** @type {import('next').NextConfig} */
const nextConfig = {
    output: "standalone",
    allowedDevOrigins: ["true"],
    env: {
        NEXT_PUBLIC_MASTER_API_URL: process.env.NEXT_PUBLIC_MASTER_API_URL,
        NEXT_PUBLIC_REDUCER_SSE_URL: process.env.NEXT_PUBLIC_REDUCER_SSE_URL,
        //
        // MASTER_API_PORT: process.env.MASTER_API_PORT,
        // MASTER_API_URL: process.env.MASTER_API_URL,
        // REDUCER_SSE_PORT: process.env.REDUCER_SSE_PORT,
        // REDUCER_SSE_URL: process.env.REDUCER_SSE_URL,
    }
};

export default nextConfig;
