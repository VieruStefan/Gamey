"use client";

import { useState } from "react";
import { Star, Gamepad2 } from "lucide-react";
import styles from "./Home.module.css";
import Link from "next/link";
import { games } from "./games";
import Navbar from "@/components/Navbar/Navbar";
import Sidebar from "@/components/Sidebar/Sidebar";
import Image from "next/image";

export default function HomePage() {
  const [selectedGame, setSelectedGame] = useState(null);
  const [platformFilter, setPlatformFilter] = useState("all");
  const [genreFilter, setGenreFilter] = useState("all");

  const filteredGames = games.filter((game) => {
    const platformMatch =
      platformFilter === "all" ||
      game.platform.includes(platformFilter.toUpperCase());
    const genreMatch =
      genreFilter === "all" ||
      game.genre.toLowerCase() === genreFilter.toLowerCase();
    return platformMatch && genreMatch;
  });

  return (
    <>
      <Navbar />
      <div className={styles.container}>
        <Sidebar
        />
        <main className={styles.main}>
          <div className={styles.header}>
            <h2 className={styles.title}>Discover Games</h2>
            <p className={styles.subtitle}>
              Find the best games for PS4, PS5, and PC with prices and reviews
            </p>
            <div className={styles.count}>
              {filteredGames.length}{" "}
              {filteredGames.length === 1 ? "game found" : "games found"}
            </div>
          </div>

          <div className={styles.gamesGrid}>
            {filteredGames.map((game) => (
              <Link
                key={game.id}
                href={`/${game.id}`}
                className={styles.gameCard}
              >
                <div className={styles.gameImageWrapper}>
                  <Image
                    src={game.image}
                    alt={game.title}
                  />
                  <div className={styles.gradientOverlay} />
                </div>
                <div className={styles.gameContent}>
                  <h3 className={styles.gameTitle}>{game.title}</h3>
                  <div className={styles.tags}>
                    {game.platform.map((p) => (
                      <span
                        key={p}
                        className={`${styles.tag} ${styles.platformTag}`}
                      >
                        {p}
                      </span>
                    ))}
                    <span className={`${styles.tag} ${styles.genreTag}`}>
                      {game.genre}
                    </span>
                  </div>
                  <div className={styles.stats}>
                    <div className={styles.rating}>
                      <Star />
                      <span>{game.rating}</span>
                      <span className={styles.ratingSmall}>
                        ({game.reviews})
                      </span>
                    </div>
                    <div className={styles.price}>
                      <p className={styles.priceSmall}>de la</p>
                      <p className={styles.priceValue}>
                        ${Math.min(...game.prices.map((p) => p.price))}
                      </p>
                    </div>
                  </div>
                </div>
              </Link>
            ))}
          </div>

          {filteredGames.length === 0 && (
            <div className={styles.noGames}>
              <Gamepad2 />
              <p>No games found</p>
              <p className={styles.noGamesSub}>
               Try adjusting the filters to find more results.
              </p>
            </div>
          )}
        </main>
      </div>
    </>
  );
}
