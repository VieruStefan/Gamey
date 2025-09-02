import {getGames} from "@/app/_lib/data"
import {Suspense} from "react"
import Loading from "./loading"
import Games from "@/app/components/Products/Games"
import Sidebar from "@/app/components/Sidebar/Sidebar"
import styles from "@/app/components/Home.module.css"
import {redirect} from "next/navigation"

export default async function HomePage({ searchParams }:
{ searchParams?: {
        [key: string]: string | undefined
}}) {
    const platform = searchParams?.platform || "all"
    const priceRange: [number, number] = [Number(searchParams?.minPrice) || 0, Number(searchParams?.maxPrice) || 500]
    const size = Number(searchParams?.size) || 18
    const page = Number(searchParams.page) || 1
    const search = searchParams.search || ""
    const dataPromise = getGames({
        page: page - 1,
        size: size,
        search: search,
    })

    async function handleSearch(formData: FormData) {
        'use server'
        const search = formData.get('search')?.toString() || ""

        const params = new URLSearchParams(searchParams)

        if (search) {
            params.set('search', search)
        } else {
            params.delete('search')
        }
        params.set('page', '1')
        params.set('size', '10')

        redirect(`/?${params.toString()}`)
    }

    return (
        <div className={styles.pageContainer}>
            <Suspense fallback={<div className="w-64 bg-slate-800 animate-pulse"/>}>
                <Sidebar initialPlatform={platform} initialPriceRange={priceRange}/>
            </Suspense>
            <div className={styles.mainContent}>
                <Suspense fallback={<Loading/>}>
                    <Games
                        data={dataPromise}
                        searchQuery={search}
                        platform={platform}
                        priceRange={priceRange}
                        currentPage={page}
                        itemsPerPage={size}
                        handleSearch={handleSearch}
                    />
                </Suspense>
            </div>
        </div>
    )
}

