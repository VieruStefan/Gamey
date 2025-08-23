"use client";

import { Search, Star, Heart } from "lucide-react";
import styles from "./Navbar.module.css";
import Link from "next/link";

export default function Navbar() {
  return (
    <nav className={styles.navbar}>
      <div className={styles.navbarContainer}>
        <Link href="/home" className={styles.logo}>
          Gamey
        </Link>

        <div className={styles.searchContainer}>
          <div className={styles.searchIconWrapper}>
            <Search className={styles.searchIcon} />
          </div>
          <input
            type="text"
            placeholder="Search games"
            className={styles.searchInput}
          />
          <button className={styles.searchButton}>Go</button>
        </div>

        <div className={styles.navLinks}>
          <Link href="/favorites" className={styles.navLink}>
            <Heart className={styles.navIcon} /> Favorites
          </Link>
          <Link href="/top-rated" className={styles.navLink}>
            <Star className={styles.navIcon} /> Top Rated
          </Link>
        </div>
      </div>
    </nav>
  );
}
