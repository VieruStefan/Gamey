"use client"
import styles from "@/app/components/Product/Profile.module.css"
import Link from "next/link"
import { ArrowLeft, ExternalLink, Star } from "lucide-react"
import type { GameTypes } from "@/app/_types/gameTypes"
import { use, useState } from "react"
import Image from "next/image"

export default function Game({ params }: { params: Promise<GameTypes> }) {
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
            <ArrowLeft />
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
                <div className={styles.tags}>
                    {game.sources?.map((source) => (
                        <span key={source.url} className={styles.platformTag}>
                            {source.platform}
                        </span>
                    ))}
                </div>
            </div>

              <div className={styles.sideSection}>
                  {game.sources?.map((source) => (
                      <div key={source.url} className={styles.priceCard}>
                          <div>
                              <p className={styles.priceStore}>
                                  {/* We now use the URL from the 'source' object */}
                                  {new URL(source.url).hostname.replace("www.", "")}
                              </p>
                              <p className={styles.priceValue}>
                                  X RON
                              </p>
                          </div>
                          <a href={source.url} className={styles.visitButton} target="_blank" rel="noopener noreferrer">
                              Vizitează <ExternalLink className="h-4 w-4" />
                          </a>
                      </div>
                  ))}
              </div>
          </div>
        </main>
      </div>
    </>
  )
}
