"use client"
import styles from "@/app/components/Product/Profile.module.css"
import Link from "next/link"
import { ArrowLeft, ExternalLink, Star } from "lucide-react"
import type { ProductTypes } from "@/app/_types/product.types"
import { use } from "react"
import Image from "next/image"

export default function Product({ product }: { product: Promise<ProductTypes> }) {
  const game = use(product)

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
              src={game.image || "/placeholder.svg"}
              alt={`image of ${game.title}`}
              width={640}
              height={360}
              style={{ objectFit: "cover" }}
              className={styles.gameImage}
            />

            <div className={styles.gameInfo}>
              <h1 className={styles.gameTitle}>{game.title}</h1>
              <div className={styles.tags}>
                <span className={styles.platformTag}>{game.platform}</span>
              </div>
              <div className={styles.rating}>
                {[...Array(5)].map((_, i) => (
                  <Star key={i} className={i < 0 ? styles.starFilled : styles.starEmpty} />
                ))}
                <span className={styles.ratingValue}>-</span>
                <span className={styles.reviews}>(No reviews)</span>
              </div>
            </div>

            <div className={styles.sideSection}>
              <div className={styles.priceCard}>
                <div>
                  <p className={styles.priceStore}>{new URL(game.url).hostname.replace("www.", "")}</p>
                  <p className={styles.priceValue}>{game.price} RON</p>
                </div>
                <a href={game.url} className={styles.visitButton} target="_blank" rel="noopener noreferrer">
                  Vizitează <ExternalLink className="h-4 w-4" />
                </a>
              </div>
            </div>
          </div>
        </main>
      </div>
    </>
  )
}
