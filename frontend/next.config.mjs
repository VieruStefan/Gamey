/** @type {import('next').NextConfig} */
const nextConfig = {
    output: 'standalone',
    allowedDevOrigins: ["http://localhost:3000"],
    env: {
        NEXT_PUBLIC_MASTER_API_URL: process.env.NEXT_PUBLIC_MASTER_API_URL,
        NEXT_PUBLIC_REDUCER_SSE_URL: process.env.NEXT_PUBLIC_REDUCER_SSE_URL,
    }
};

export default nextConfig;
