"use client"
import styles from "@/app/components/Product/Profile.module.css"
import Link from "next/link"
import {ArrowLeft, ExternalLink} from "lucide-react"
import type {GameTypes} from "@/app/_types/gameTypes"
import {use, useState} from "react"
import Image from "next/image"
import PriceHistoryChart from "@/app/components/PriceHistoryChart/PriceHistoryChart";

export default function Game({params}: { params: Promise<GameTypes> }) {
    const game = use(params)
    const [imageError, setImageError] = useState(false)

    const isValidUrl = (url: string | undefined | null): boolean => {
        if (!url || url === "undefined" || url === "null") return false
        try {
            new URL(url)
            return true
        } catch {
            return false
        }
    }

    const getImageSrc = () => {
        if (imageError || !isValidUrl(game.image)) {
            return "/game-placeholder.png"
        }
        return game.image
    }

    return (
        <>
            <div className={styles.pageWrapper}>
                <main className={styles.main}>
                    <Link href="/" className={styles.backButton}>
                        <ArrowLeft/>
                        Back to Games
                    </Link>

                    <div className={styles.grid}>
                        <Image
                            src={getImageSrc() || "/placeholder.svg"}
                            alt={`image of ${game.title}`}
                            width={640}
                            height={360}
                            className={styles.gameImageCover}
                            onError={() => setImageError(true)}
                            unoptimized={true}
                        />

                        <div className={styles.gameInfo}>
                            <h1 className={styles.gameTitle}>{game.title}</h1>

                            <div className={styles.sourcesGrid}>
                                {game.sources?.map((source) => (
                                    <div key={source.url} className={styles.priceCard}>
                                        <div>
                                            <p className={styles.priceStore}>
                                                {new URL(source.url).hostname.replace("www.", "")}
                                            </p>
                                        </div>
                                        <div className={styles.cardFooter}>
                                            <span className={styles.platformTag}>{source.platform}</span>
                                            <a href={source.url} className={styles.visitButton} target="_blank" rel="noopener noreferrer">
                                                Vizitează <ExternalLink className="h-4 w-4" />
                                            </a>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {game.sources && game.sources.length > 0 && (
                            <div className={styles.chartSection}>
                                <PriceHistoryChart sources={game.sources} />
                            </div>
                        )}
                    </div>
                </main>
            </div>
        </>
    )
}
