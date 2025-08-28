'use client'
import styles from "@/app/components/Products/Home.module.css";
import Link from "next/link";
import Image from "next/image";
import {Gamepad2, Star} from "lucide-react";
import {ProductTypes} from "@/app/_types/product.types";
import {use} from "react";

export default function Products({products}: { products: Promise<ProductTypes[]> }) {
    const allProducts = use(products)
    return (<>
        <div className={styles.container}>
            <main className={styles.main}>
                <div className={styles.header}>
                    <h2 className={styles.title}>Discover Games</h2>
                    <p className={styles.subtitle}>
                        Find the best games for PS4, PS5, and PC with prices and reviews
                    </p>
                </div>

                <div className={styles.gamesGrid}>
                    {allProducts.map((product) => (
                        <Link key={product.id} href={`/${product.id}`} className={styles.gameCard}>
                            <div className={styles.gameImageWrapper}>
                                <Image src={product.image}
                                       alt={product.title}
                                       width="205"
                                       height="273"
                                       style={{objectFit: 'cover'}}
                                       priority={true}
                                       className={styles.gameImage}
                                />
                                <div className={styles.gradientOverlay}/>
                            </div>
                            <div className={styles.gameContent}>
                                <h3 className={styles.gameTitle}>{product.title}</h3>
                                <div className={styles.tags}>
                                    <span className={`${styles.tag} ${styles.platformTag}`}>
                                        {product.platform}
                                    </span>
                                </div>
                                <div className={styles.stats}>
                                    <div className={styles.rating}>
                                        <Star/>
                                        <span>TODO</span>
                                        <span className={styles.ratingSmall}>(TODO)</span>
                                    </div>
                                    <div className={styles.price}>
                                        <p className={styles.priceValue}>{product.price} Lei</p>
                                    </div>
                                </div>
                            </div>
                        </Link>))}
                </div>

                {allProducts.length === 0 && (<div className={styles.noGames}>
                    <Gamepad2/>
                    <p>No games found</p>
                    <p className={styles.noGamesSub}>
                        Try adjusting the filters to find more results.
                    </p>
                </div>)}
            </main>
        </div>
    </>)
}