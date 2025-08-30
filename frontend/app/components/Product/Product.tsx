"use client"
import styles from "@/app/components/Product/Profile.module.css"
import Link from "next/link"
import { ArrowLeft, ExternalLink, Star } from "lucide-react"
import type { ProductTypes } from "@/app/_types/product.types"
import { use, useState } from "react"
import Image from "next/image"

export default function Product({ product }: { product: Promise<ProductTypes> }) {
  const game = use(product)
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
                <span className={styles.platformTag}>{game.platform}</span>
              </div>
              <div className={styles.rating}>
                {[...Array(5)].map((_, i) => (
                  <Star key={i} className={i < game.rating ? styles.starFilled : styles.starEmpty} />
                ))}
                <span className={styles.ratingValue}>{game.rating}</span>
                <span className={styles.reviews}>({game.reviews} reviews)</span>
              </div>
            </div>

            <div className={styles.sideSection}>
              <div className={styles.priceCard}>
                <div>
                  <p className={styles.priceStore}>
                    {isValidUrl(game.url) ? new URL(game.url).hostname.replace("www.", "") : "Unknown Store"}
                  </p>
                  <p className={styles.priceValue}>{game.price} RON</p>
                </div>
                {isValidUrl(game.url) && (
                  <a href={game.url} className={styles.visitButton} target="_blank" rel="noopener noreferrer">
                    Vizitează <ExternalLink className="h-4 w-4" />
                  </a>
                )}
              </div>
            </div>
          </div>
        </main>
      </div>
    </>
  )
}
