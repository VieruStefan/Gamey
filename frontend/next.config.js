// @ts-check

/** @type {import('next').NextConfig} */
const nextConfig = {
    output: 'standalone',
    allowedDevOrigins: ["http://localhost:3000"],
    env: {
        NEXT_PUBLIC_GATEWAY_URL: process.env.NEXT_PUBLIC_GATEWAY_URL,
    },
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'www.lumea-jocurilor.ro',
                port: '',
                pathname: '/wareImages/**',
            },
            {
                protocol: 'https',
                hostname: 'www.jocurinoi.ro',
                port: '',
                pathname: '/image/**',
            },
            {
                protocol: 'https',
                hostname: 'b.scdn.gr',
                port: '',
                pathname: '/images/**',
            },
            {
                protocol: 'https',
                hostname: '*.scdn.gr',
                port: '',
                pathname: '/images/**',
            },
            {
                protocol: 'https',
                hostname: 'gomagcdn.ro',
                port: '',
                pathname: '/**/mobile-zone.ro/**',
            },
            {
                protocol: 'https',
                hostname: '*.cel.ro',
                port: '',
                pathname: '/images/**',
            },
            {
                protocol: 'https',
                hostname: 'www.buy2play.ro',
                port: '',
                pathname: '/**/uploads/**',
            },
        ],
    },
};

module.exports = nextConfig