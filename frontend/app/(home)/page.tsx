import { getProducts } from "@/app/_lib/data"
import { Suspense } from "react"
import Loading from "./loading"
import Products from "@/app/components/Products/Products"
import Sidebar from "@/app/components/Sidebar/Sidebar"

export default async function HomePage({
                                           searchParams,
                                       }: {
    searchParams?: {
        platform?: string
        minPrice?: string
        maxPrice?: string
        search?: string
    }
}) {
    const platform = searchParams?.platform || "all"
    const priceRange: [number, number] = [Number(searchParams?.minPrice) || 0, Number(searchParams?.maxPrice) || 500]
    const searchQuery = searchParams?.search || ""

    const products = getProducts()

    return (
        <div className="bg-[rgba(15,23,42,1)]" style={{ display: "flex" }}>
            <Sidebar initialPlatform={platform} initialPriceRange={priceRange} />
            <div style={{ flex: 1, padding: "1rem" }}>
                <Suspense fallback={<Loading />}>
                    <Products products={products} searchQuery={searchQuery} platform={platform} priceRange={priceRange} />
                </Suspense>
            </div>
        </div>
    )
}
