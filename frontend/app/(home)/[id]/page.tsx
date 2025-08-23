import styles from "./Profile.module.css";
import {ArrowLeft, ExternalLink, Star} from "lucide-react";
import {games} from "../games";
import Link from "next/link";
import Navbar from "@/components/Navbar/Navbar";
import Image from "next/image";

export default async function GamePage(
    {
        params
    }: {
        params: Promise<{ id: number }>
    }) {
    const {id} = await params;
    const game = games.find((value) => value.id === id);
    if (!game) return <div>Game not found</div>;

    return (<>
        <Navbar/>
        <div className={styles.pageWrapper}>
            <main className={styles.main}>
                <Link href="/" className={styles.backButton}>
                    <ArrowLeft/>
                    Back to Games
                </Link>

                <div className={styles.grid}>
                    <Image
                        src={game.image}
                        alt={game.title}
                        className={styles.gameImage}
                    />

                    <div className={styles.gameInfo}>
                        <h1 className={styles.gameTitle}>{game.title}</h1>
                        <div className={styles.tags}>
                            {game.platform.map((p) => (<span key={p} className={styles.platformTag}>
                    {p}
                  </span>))}
                            <span className={styles.genreTag}>{game.genre}</span>
                        </div>
                        <div className={styles.rating}>
                            {[...Array(5)].map((_, i) => (<Star
                                key={i}
                                className={i < Math.floor(game.rating) ? styles.starFilled : styles.starEmpty}
                            />))}
                            <span className={styles.ratingValue}>{game.rating}</span>
                            <span className={styles.reviews}>
                  ({game.reviews} recenzii)
                </span>
                        </div>
                    </div>

                    <div className={styles.sideSection}>
                        {game.prices.map((price, index) => (<div key={index} className={styles.priceCard}>
                            <div>
                                <p className={styles.priceStore}>{price.store}</p>
                                <p className={styles.priceValue}>${price.price}</p>
                            </div>
                            <a
                                href={price.url}
                                className={styles.visitButton}
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                Vizitează <ExternalLink className="h-4 w-4"/>
                            </a>
                        </div>))}
                        <div className={styles.reviewsSection}>
                            <h2>Reviews</h2>
                            <p>{game.reviews} total reviews</p>

                            {game.comments && game.comments.length > 0 && (<div className={styles.commentList}>
                                {game.comments.map((comment, index) => (
                                    <div key={index} className={styles.commentCard}>
                                        <p className={styles.commentUsername}>
                                            {comment.username}:
                                        </p>
                                        <p className={styles.commentText}>{comment.text}</p>
                                    </div>))}
                            </div>)}
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </>);
}
