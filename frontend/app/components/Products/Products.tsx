"use client"
import styles from "@/app/components/Products/Home.module.css"
import Link from "next/link"
import Image from "next/image"
import { Gamepad2, Star } from "lucide-react"
import type { ProductTypes } from "@/app/_types/product.types"
import { use, useMemo } from "react"

export default function Products({
                                     products,
                                     searchQuery = "",
                                     platform = "all",
                                     priceRange = [0, 500],
                                 }: {
    products: Promise<ProductTypes[]>
    searchQuery?: string
    platform?: string
    priceRange?: [number, number]
}) {
    const allProducts = use(products)

    const filteredProducts = useMemo(() => {
        return allProducts.filter((product) => {
            // Search filter
            const matchesSearch = !searchQuery || product.title.toLowerCase().includes(searchQuery.toLowerCase())

            // Platform filter
            const matchesPlatform = platform === "all" || product.platform.toLowerCase().includes(platform.toLowerCase())

            // Price filter
            const productPrice = Number.parseFloat(product.price)
            const matchesPrice = productPrice >= priceRange[0] && productPrice <= priceRange[1]

            return matchesSearch && matchesPlatform && matchesPrice
        })
    }, [allProducts, searchQuery, platform, priceRange])

    return (
        <>
            <div className={styles.container}>
                <main className={styles.main}>
                    <div className={styles.header}>
                        <h2 className={styles.title}>{searchQuery ? `Search results for "${searchQuery}"` : "Discover Games"}</h2>
                        <p className={styles.subtitle}>
                            {searchQuery
                                ? `Found ${filteredProducts.length} games matching your search`
                                : "Find the best games for PS4, PS5, and PC with prices and reviews"}
                        </p>
                    </div>

                    <div className={styles.gamesGrid}>
                        {filteredProducts.map((product) => (
                            <Link key={product.id} href={`/${product.id}`} className={styles.gameCard}>
                                <div className={styles.gameImageWrapper}>
                                    <Image
                                        src={product.image || "/placeholder.svg"}
                                        alt={product.title}
                                        width="205"
                                        height="273"
                                        style={{ objectFit: "cover" }}
                                        priority={true}
                                        className={styles.gameImage}
                                    />
                                    <div className={styles.gradientOverlay} />
                                </div>
                                <div className={styles.gameContent}>
                                    <h3 className={styles.gameTitle}>{product.title}</h3>
                                    <div className={styles.tags}>
                                        <span className={`${styles.tag} ${styles.platformTag}`}>{product.platform}</span>
                                    </div>
                                    <div className={styles.stats}>
                                        <div className={styles.rating}>
                                            <Star />
                                            <span>TODO</span>
                                            <span className={styles.ratingSmall}>(TODO)</span>
                                        </div>
                                        <div className={styles.price}>
                                            <p className={styles.priceValue}>{product.price} Lei</p>
                                        </div>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>

                    {filteredProducts.length === 0 && (
                        <div className={styles.noGames}>
                            <Gamepad2 />
                            <p>{searchQuery ? "No games found for your search" : "No games found"}</p>
                            <p className={styles.noGamesSub}>
                                {searchQuery
                                    ? "Try a different search term or adjust the filters."
                                    : "Try adjusting the filters to find more results."}
                            </p>
                        </div>
                    )}
                </main>
            </div>
        </>
    )
}
