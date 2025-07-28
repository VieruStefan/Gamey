/** @type {import('next').NextConfig} */
const nextConfig = {
    output: "standalone",
    allowedDevOrigins: ["true"],
    env: {
        MASTER_API_PORT: process.env.MASTER_API_PORT,
        MASTER_API_URL: process.env.MASTER_API_URL,
        REDUCER_SSE_PORT: process.env.REDUCER_SSE_PORT,
        REDUCER_SSE_URL: process.env.REDUCER_SSE_URL,
    }
};

export default nextConfig;
