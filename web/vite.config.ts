import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";
import tailwindcss from "@tailwindcss/vite";
import { resolve } from "node:path";
// https://vite.dev/config/

export default defineConfig({
  plugins: [tailwindcss(), react()],
    server: {
    port: 8080,
    strictPort: true,
    proxy: {
      // Klici na /api gredo na pravi strežnik. Brskalniku je videti,
      // da vse prihaja z istega naslova, zato piškotek seje deluje.
      "/api": {
        target: "https://fp.kokosek.si",
        changeOrigin: true,
      },
    },
  },
  base: "/",
  build: {
    outDir: "dist",
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(import.meta.dirname, "index.html"),
      },
    },
  },
});
