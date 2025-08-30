import type { ProductTypes } from "@/app/_types/product.types"

const mockProducts: ProductTypes[] = [
  {
    id: "1",
    name: "Sample Product 1",
    price: 29.99,
    description: "This is a sample product for development",
    image: "/generic-product-display.png",
    category: "Electronics",
  },
  {
    id: "2",
    name: "Sample Product 2",
    price: 49.99,
    description: "Another sample product for development",
    image: "/sample-product-2.png",
    category: "Accessories",
  },
]

export const getProducts = async (): Promise<ProductTypes[]> => {
  try {
    const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL
    if (!gatewayUrl) {
      console.warn("[v0] NEXT_PUBLIC_GATEWAY_URL not set, using mock data")
      return mockProducts
    }

    const response = await fetch(`${gatewayUrl}/gamedata/api/products`)

    if (!response.ok) {
      console.error("[v0] API response not ok:", response.status, response.statusText)
      return mockProducts
    }

    const contentType = response.headers.get("content-type")
    if (!contentType || !contentType.includes("application/json")) {
      console.error("[v0] API returned non-JSON response:", contentType)
      const text = await response.text()
      console.error("[v0] Response body:", text.substring(0, 200))
      return mockProducts
    }

    const data = await response.json()
    return Array.isArray(data) ? data : mockProducts
  } catch (error) {
    console.error("[v0] Error fetching products:", error)
    return mockProducts
  }
}

export const getProduct = async (id: string): Promise<ProductTypes | null> => {
  try {
    const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL
    if (!gatewayUrl) {
      console.warn("[v0] NEXT_PUBLIC_GATEWAY_URL not set, using mock data")
      return mockProducts.find((p) => p.id === id) || mockProducts[0]
    }

    const response = await fetch(`${gatewayUrl}/gamedata/api/products/${id}`)

    if (!response.ok) {
      console.error("[v0] API response not ok:", response.status, response.statusText)
      return mockProducts.find((p) => p.id === id) || mockProducts[0]
    }

    const contentType = response.headers.get("content-type")
    if (!contentType || !contentType.includes("application/json")) {
      console.error("[v0] API returned non-JSON response:", contentType)
      const text = await response.text()
      console.error("[v0] Response body:", text.substring(0, 200))
      return mockProducts.find((p) => p.id === id) || mockProducts[0]
    }

    const data = await response.json()
    return data
  } catch (error) {
    console.error("[v0] Error fetching product:", error)
    return mockProducts.find((p) => p.id === id) || mockProducts[0]
  }
}
