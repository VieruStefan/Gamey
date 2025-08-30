import { getProducts } from "@/app/_lib/data"
import { Suspense } from "react"
import Loading from "./loading"
import Products from "@/app/components/Products/Products"
import Sidebar from "@/app/components/Sidebar/Sidebar"
import styles from "@/app/components/Home.module.css"

export default async function HomePage({
  searchParams,
}: {
  searchParams?: {
    platform?: string
    minPrice?: string
    maxPrice?: string
    search?: string
    page?: string
  }
}) {
  const platform = searchParams?.platform || "all"
  const priceRange: [number, number] = [Number(searchParams?.minPrice) || 0, Number(searchParams?.maxPrice) || 500]
  const searchQuery = searchParams?.search || ""
  const currentPage = Number(searchParams?.page) || 1

  const products = getProducts()

  return (
    <div className={styles.pageContainer}>
      <Suspense fallback={<div className="w-64 bg-slate-800 animate-pulse" />}>
        <Sidebar initialPlatform={platform} initialPriceRange={priceRange} />
      </Suspense>
      <div className={styles.mainContent}>
        <Suspense fallback={<Loading />}>
          <Products
            products={products}
            searchQuery={searchQuery}
            platform={platform}
            priceRange={priceRange}
            currentPage={currentPage}
          />
        </Suspense>
      </div>
    </div>
  )
}
