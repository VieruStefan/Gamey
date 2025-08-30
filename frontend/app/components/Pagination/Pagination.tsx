"use client"
import { ChevronLeft, ChevronRight } from "lucide-react"
import { useRouter, useSearchParams } from "next/navigation"
import styles from "./Pagination.module.css"

interface PaginationProps {
  currentPage: number
  totalPages: number
  totalItems: number
  itemsPerPage: number
}

export default function Pagination({ currentPage, totalPages, totalItems, itemsPerPage }: PaginationProps) {
  const router = useRouter()
  const searchParams = useSearchParams()

  const updatePage = (page: number) => {
    const params = new URLSearchParams(searchParams.toString())
    if (page === 1) {
      params.delete("page")
    } else {
      params.set("page", page.toString())
    }
    router.push(`?${params.toString()}`)
  }

  const startItem = (currentPage - 1) * itemsPerPage + 1
  const endItem = Math.min(currentPage * itemsPerPage, totalItems)

  // Generate page numbers to show
  const getPageNumbers = () => {
    const pages = []
    const maxVisible = 5

    if (totalPages <= maxVisible) {
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i)
      }
    } else {
      if (currentPage <= 3) {
        for (let i = 1; i <= 4; i++) {
          pages.push(i)
        }
        pages.push("...")
        pages.push(totalPages)
      } else if (currentPage >= totalPages - 2) {
        pages.push(1)
        pages.push("...")
        for (let i = totalPages - 3; i <= totalPages; i++) {
          pages.push(i)
        }
      } else {
        pages.push(1)
        pages.push("...")
        for (let i = currentPage - 1; i <= currentPage + 1; i++) {
          pages.push(i)
        }
        pages.push("...")
        pages.push(totalPages)
      }
    }

    return pages
  }

  if (totalPages <= 1) return null

  return (
    <div className={styles.pagination}>
      {/* Results info */}
      <div className={styles.info}>
        Showing {startItem}-{endItem} of {totalItems} results
      </div>

      {/* Pagination controls */}
      <div className={styles.controls}>
        {/* Previous button */}
        <button onClick={() => updatePage(currentPage - 1)} disabled={currentPage === 1} className={styles.button}>
          <ChevronLeft size={16} />
          Previous
        </button>

        {/* Page numbers */}
        <div className={styles.pageNumbers}>
          {getPageNumbers().map((page, index) => (
            <div key={index}>
              {page === "..." ? (
                <span className={styles.ellipsis}>...</span>
              ) : (
                <button
                  onClick={() => updatePage(page as number)}
                  className={`${styles.pageButton} ${currentPage === page ? styles.active : ""}`}
                >
                  {page}
                </button>
              )}
            </div>
          ))}
        </div>

        {/* Next button */}
        <button
          onClick={() => updatePage(currentPage + 1)}
          disabled={currentPage === totalPages}
          className={styles.button}
        >
          Next
          <ChevronRight size={16} />
        </button>
      </div>
    </div>
  )
}
