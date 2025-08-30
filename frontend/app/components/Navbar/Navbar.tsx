"use client"

import type React from "react"

import styles from "./Navbar.module.css"
import Link from "next/link"

export default function Navbar() {

    return (
        <nav className={styles.navbar}>
                <Link href="/" className={styles.logo}>
                    Gamey
                </Link>
        </nav>
    )
}
