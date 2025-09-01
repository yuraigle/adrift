import tailwindcss from '@tailwindcss/vite'

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    compatibilityDate: '2025-07-15',
    css: ['~/assets/css/main.css'],
    devtools: { enabled: true },
    modules: ['@nuxt/eslint', '@nuxtjs/google-fonts', '@pinia/nuxt'],
    typescript: {
        typeCheck: true,
        strict: true
    },
    appConfig: {
        APP_VERSION: process.env.npm_package_version,
        API_BASE: 'https://adrift.orlov.bar/api',
        S3_BASE: 'https://bucket-5f7c65.s3.cloud.ru/',
    },
    vite: {
        plugins: [
            tailwindcss(),
        ],
    },
    imports: {
        dirs: [
            'types/*.ts', // Auto-import types from 'types/'
            'types/**/*.ts' // Auto-import types from nested 'types/' subdirectories
        ]
    },
    app: {
        head: {
            title: 'Adrift', // default fallback title
            htmlAttrs: {
                lang: 'en',
            },
            link: [
                { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
            ]
        },
        pageTransition: { name: 'page', mode: 'out-in' }
    },
    nitro: {
        prerender: {
            crawlLinks: true,
            routes: ["/search"],
        },
    },
    hooks: {
        "prerender:routes"(ctx) {
            const base = "https://adrift.orlov.bar/api";

            fetch(base + '/ads/crawl', { headers: { 'Authorization': 'CRAWLER' } })
                .then((res) => res.json())
                .then((data: Array<string>) => data.forEach((x: string) => ctx.routes.add('/a' + x)));

            fetch(base + '/categories/crawl', { headers: { 'Authorization': 'CRAWLER' } })
                .then((res) => res.json())
                .then((data: Array<string>) => data.forEach((x: string) => ctx.routes.add('/category' + x)));
        },
    },
    googleFonts: {
        families: {
            'Manrope': true,
        },
        download: true,
    }
})
