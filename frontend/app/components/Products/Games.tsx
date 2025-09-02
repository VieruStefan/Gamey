"use client"
import styles from "@/app/components/Products/Products.module.css"
import Link from "next/link"
import Image from "next/image"
import {Search, Star} from "lucide-react"
import type {GameTypes, Page} from "@/app/_types/gameTypes"
import {use, useState} from "react"
import Pagination from "@/app/components/Pagination/Pagination";

export default function Games({
                                  data,
                                  searchQuery = "",
                                  platform = "all",
                                  priceRange = [0, 500],
                                  currentPage = 0,
                                  itemsPerPage = 12,
                                  handleSearch,
                              }: {
    data: Promise<Page<GameTypes>>
    searchQuery?: string
    platform?: string
    priceRange?: [number, number]
    currentPage?: number
    itemsPerPage?: number
    handleSearch: (formData: FormData) => Promise<void>
}) {
    const page = use(data)
    const games = page.content
    const totalPages = page.totalPages
    const totalItems = page.totalElements
    const [failedImages, setFailedImages] = useState<Set<string>>(new Set())

    const isValidUrl = (url: string | undefined | null): boolean => {
        if (!url || url === "undefined" || url === "null") return false
        try {
            new URL(url)
            return true
        } catch {
            return false
        }
    }

    const getImageSrc = (game: GameTypes) => {
        if (failedImages.has(game.gameId) || !isValidUrl(game.image)) {
            return "/game-placeholder.png"
        }
        return game.image
    }

    const startIndex = (currentPage - 1) * itemsPerPage
    const endIndex = startIndex + itemsPerPage

    const handleImageError = (productId: string) => {
        setFailedImages((prev) => new Set(prev).add(productId))
    }

    return (
        <>
            <div className={styles.container}>
                <main className={styles.main}>
                    <div className={styles.header}>
                        <h2 className={styles.title}>{searchQuery ? `Search results for "${searchQuery}"` : "Discover Games"}</h2>
                        <p className={styles.subtitle}>
                            {searchQuery
                                ? `Found X games matching your search`
                                : "Find the best games for PS4, PS5, and PC with prices and reviews"}
                        </p>
                        <form action={handleSearch} className={styles.searchContainer}>
                            <div className={styles.searchIconWrapper}>
                                <Search className={styles.searchIcon}/>
                            </div>
                            <input
                                type="text"
                                name="search"
                                placeholder="Search games"
                                className={styles.searchInput}
                                defaultValue={searchQuery}
                                key={searchQuery}
                            />
                            <button type="submit" className={styles.searchButton}>
                                Go
                            </button>
                        </form>
                    </div>

                    <div className={styles.gamesGrid}>
                        {games.map((game) => (
                            <Link key={game.gameId} href={`/${game.gameId}`} className={styles.gameCard}>
                                <div className={styles.gameImageWrapper}>
                                    <Image
                                        src={getImageSrc(game) || "/placeholder.svg"}
                                        alt={game.title}
                                        width="205"
                                        height="273"
                                        priority={true}
                                        className={styles.gameImage}
                                        onError={() => handleImageError(game.gameId)}
                                        unoptimized={true}
                                    />
                                    <div className={styles.gradientOverlay}/>
                                </div>
                                <div className={styles.gameContent}>
                                    <h3 className={styles.gameTitle}>{game.title}</h3>
                                    <div className={styles.tags}>
                                        {/*<span className={`${styles.tag} ${styles.platformTag}`}>{product.platform}</span>*/}
                                    </div>
                                    <div className={styles.stats}>
                                        <div className={styles.rating}>
                                            <Star/>
                                            {/*<span>{product.rating}</span>*/}
                                            {/*<span className={styles.ratingSmall}>({product.ratingCount})</span>*/}
                                        </div>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        totalItems={totalItems}
                        itemsPerPage={itemsPerPage}
                    />
                </main>
            </div>
        </>
    )
}
