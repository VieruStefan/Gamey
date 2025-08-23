"use client"

import { useState } from "react"
import { Filter } from "lucide-react"
import styles from "./Sidebar.module.css"

export default function Sidebar() {
  const [platformFilter, setPlatformFilter] = useState("all")
  const [genreFilter, setGenreFilter] = useState("all")

  return (
    <aside className={styles.sidebar}>
      <div className={styles.header}>
        <div className={styles.title}>
          <div className={styles.iconWrapper}>
            <Filter className={styles.icon} />
          </div>
          Filter Games
        </div>
      </div>

      <div className={styles.content}>
        <div className={styles.filterGroup}>
          <label className={styles.label}>Platform</label>
          <select
            className={styles.select}
            value={platformFilter}
            onChange={(e) => setPlatformFilter(e.target.value)}
          >
            <option value="all">All Platforms</option>
            <option value="playstation">PlayStation (PS4/PS5)</option>
            <option value="pc">PC</option>
          </select>
        </div>

        <div className={styles.filterGroup}>
          <label className={styles.label}>Select a game genr</label>
          <select
            className={styles.select}
            value={genreFilter}
            onChange={(e) => setGenreFilter(e.target.value)}
          >
            <option value="all">All genr</option>
            <option value="action">Action</option>
            <option value="rpg">RPG</option>
            <option value="horror">Horror</option>
          </select>
        </div>

        <div className={styles.activeFilters}>
          {platformFilter !== "all" && (
            <span className={styles.badge}>
              {platformFilter === "playstation" ? "PlayStation" : "PC"}
            </span>
          )}
          {genreFilter !== "all" && (
            <span className={styles.badge}>
              {genreFilter.charAt(0).toUpperCase() + genreFilter.slice(1)}
            </span>
          )}
          {platformFilter === "all" && genreFilter === "all" && (
            <span className={styles.noFilters}>No active filters</span>
          )}
        </div>
      </div>
    </aside>
  )
}
