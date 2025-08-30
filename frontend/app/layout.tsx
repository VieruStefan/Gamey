import { Geist, Geist_Mono } from "next/font/google"
import { Suspense } from "react"
import Navbar from "@/app/components/Navbar/Navbar"
import "./globals.css"

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
})

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
})

export const metadata = {
  title: "Gamey",
  description: "game scraping",
}

export default function RootLayout({ children }) {
  return (
    <html lang="en" className="dark">
      <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
        <Suspense fallback={<div className="h-16 bg-slate-900" />}>
          <Navbar />
        </Suspense>
        {children}
      </body>
    </html>
  )
}
