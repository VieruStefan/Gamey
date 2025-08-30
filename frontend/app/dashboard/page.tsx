import { Suspense } from "react"
import { SseComponent } from "@/app/components/Sse/SseComponent"

export default function Page() {
  return (
    <main>
      <h1>My Application</h1>
      <Suspense fallback={<div className="animate-pulse bg-slate-800 h-32 rounded" />}>
        <SseComponent />
      </Suspense>
    </main>
  )
}
