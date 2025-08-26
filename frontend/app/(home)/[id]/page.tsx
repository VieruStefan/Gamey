import styles from "./Profile.module.css";
import {ArrowLeft, ExternalLink, Star} from "lucide-react";
import Link from "next/link";
import Navbar from "@/components/Navbar/Navbar";
import {getProduct} from "@/_lib/data";

export default async function GamePage(
    {
        params
    }: {
        params: Promise<{ id: string }>
    }) {
    const { id } = await params;
    const game = await getProduct(id);


    if (!game) return <div>Game not found</div>;

    return (
        <>
            <div className={styles.pageWrapper}>
                <main className={styles.main}>
                    <Link href="/" className={styles.backButton}>
                        <ArrowLeft />
                        Back to Games
                    </Link>

                    <div className={styles.grid}>
                        {/*<Image src={"/placeholder.png"} alt={game.title} className={styles.gameImage} />*/}

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
                                    <p className={styles.priceStore}>
                                        {new URL(game.url).hostname.replace("www.", "")}
                                    </p>
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
    );
}
