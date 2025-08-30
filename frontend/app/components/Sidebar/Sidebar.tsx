"use client"

import type React from "react"

import { usePathname, useRouter, useSearchParams } from "next/navigation"
import { useCallback, useState, useEffect } from "react"
import { Filter, RefreshCcw } from "lucide-react"
import styles from "./Sidebar.module.css"

interface SidebarProps {
  initialPlatform: string
  initialPriceRange: [number, number]
}

export default function Sidebar({ initialPlatform, initialPriceRange }: SidebarProps) {
  const router = useRouter()
  const pathname = usePathname()
  const searchParams = useSearchParams()

  const currentPlatform = searchParams.get("platform") || initialPlatform
  const currentMaxPrice = Number(searchParams.get("maxPrice")) || initialPriceRange[1]
  const currentMinPrice = Number(searchParams.get("minPrice")) || initialPriceRange[0]

  const [minSliderValue, setMinSliderValue] = useState(currentMinPrice)
  const [maxSliderValue, setMaxSliderValue] = useState(currentMaxPrice)

  useEffect(() => {
    setMinSliderValue(currentMinPrice)
    setMaxSliderValue(currentMaxPrice)
  }, [currentMinPrice, currentMaxPrice])

  useEffect(() => {
    const handler = setTimeout(() => {
      if (minSliderValue !== currentMinPrice || maxSliderValue !== currentMaxPrice) {
        const newQuery = createQueryString([
          { name: "minPrice", value: String(minSliderValue) },
          { name: "maxPrice", value: String(maxSliderValue) },
        ])
        router.push(pathname + "?" + newQuery)
      }
    }, 300)

    return () => {
      clearTimeout(handler)
    }
  }, [minSliderValue, maxSliderValue, router, pathname, currentMinPrice, currentMaxPrice])

  const createQueryString = useCallback(
    (paramsToUpdate: { name: string; value: string }[]) => {
      const params = new URLSearchParams(searchParams.toString())
      paramsToUpdate.forEach(({ name, value }) => {
        params.set(name, value)
      })
      return params.toString()
    },
    [searchParams],
  )

  const handlePlatformChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newPlatform = e.target.value
    router.push(pathname + "?" + createQueryString([{ name: "platform", value: newPlatform }]))
  }

  const handleMinSliderChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newMin = Number(e.target.value)
    if (newMin <= maxSliderValue) {
      setMinSliderValue(newMin)
    }
  }

  const handleMaxSliderChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newMax = Number(e.target.value)
    if (newMax >= minSliderValue) {
      setMaxSliderValue(newMax)
    }
  }

  const resetFilters = () => {
    router.push(pathname)
  }

  return (
    <aside className={styles.sidebar}>
      <div className={styles.header} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <div className={styles.title} style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
          <div className={styles.iconWrapper}>
            <Filter className={styles.icon} />
          </div>
          Filter Games
        </div>

        <button
          className={styles.resetButton}
          onClick={resetFilters}
          style={{ display: "flex", alignItems: "center", gap: "0.3rem" }}
        >
          <RefreshCcw className={styles.icon} />
        </button>
      </div>

      <div className={styles.content}>
        <div className={styles.filterGroup}>
          <label className={styles.label}>Platform</label>
          <select className={styles.select} value={currentPlatform} onChange={handlePlatformChange}>
            <option value="all">All Platforms</option>
            <option value="playstation">PlayStation (PS4/PS5)</option>
            <option value="pc">PC</option>
          </select>
        </div>

        <div className={styles.filterGroup}>
          <label className={styles.label}>Price Range</label>
          <div style={{ display: "flex", flexDirection: "column", gap: "0.75rem" }}>
            <div style={{ display: "flex", justifyContent: "space-between", fontSize: "0.875rem", color: "#64748b" }}>
              <span>Min: {minSliderValue} RON</span>
              <span>Max: {maxSliderValue} RON</span>
            </div>

            <div style={{ display: "flex", flexDirection: "column", gap: "0.5rem" }}>
              <div style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
                <span style={{ fontSize: "0.75rem", minWidth: "30px" }}>Min</span>
                <input
                  type="range"
                  min={0}
                  max={700}
                  value={minSliderValue}
                  onChange={handleMinSliderChange}
                  style={{
                    flex: 1,
                    height: "6px",
                    borderRadius: "5px",
                    background: `linear-gradient(to right, #4f46e5 0%, #4f46e5 ${(minSliderValue / 700) * 100}%, #334155 ${(minSliderValue / 700) * 100}%, #334155 100%)`,
                    appearance: "none",
                    outline: "none",
                  }}
                />
              </div>

              <div style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
                <span style={{ fontSize: "0.75rem", minWidth: "30px" }}>Max</span>
                <input
                  type="range"
                  min={0}
                  max={700}
                  value={maxSliderValue}
                  onChange={handleMaxSliderChange}
                  style={{
                    flex: 1,
                    height: "6px",
                    borderRadius: "5px",
                    background: `linear-gradient(to right, #334155 0%, #334155 ${(maxSliderValue / 700) * 100}%, #4f46e5 ${(maxSliderValue / 700) * 100}%, #4f46e5 100%)`,
                    appearance: "none",
                    outline: "none",
                  }}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </aside>
  )
}
