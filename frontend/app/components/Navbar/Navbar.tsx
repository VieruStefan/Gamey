"use client"

import type React from "react"

import { Search } from "lucide-react"
import styles from "./Navbar.module.css"
import Link from "next/link"
import { useRouter, useSearchParams, usePathname } from "next/navigation"
import { useState, useCallback } from "react"

export default function Navbar() {
    const router = useRouter()
    const pathname = usePathname()
    const searchParams = useSearchParams()
    const [searchQuery, setSearchQuery] = useState(searchParams.get("search") || "")

    const createQueryString = useCallback(
        (name: string, value: string) => {
            const params = new URLSearchParams(searchParams.toString())
            if (value) {
                params.set(name, value)
            } else {
                params.delete(name)
            }
            return params.toString()
        },
        [searchParams],
    )

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault()
        const queryString = createQueryString("search", searchQuery)
        router.push(pathname + (queryString ? "?" + queryString : ""))
    }

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(e.target.value)
    }

    return (
        <nav className={styles.navbar}>
            <div className={styles.navbarContainer}>
                <Link href="/" className={styles.logo}>
                    Gamey
                </Link>

                <form onSubmit={handleSearch} className={styles.searchContainer}>
                    <div className={styles.searchIconWrapper}>
                        <Search className={styles.searchIcon} />
                    </div>
                    <input
                        type="text"
                        placeholder="Search games"
                        className={styles.searchInput}
                        value={searchQuery}
                        onChange={handleInputChange}
                    />
                    <button type="submit" className={styles.searchButton}>
                        Go
                    </button>
                </form>
            </div>
        </nav>
    )
}
