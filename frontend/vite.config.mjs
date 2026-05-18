import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'
import path from 'node:path'
import { createHazelGymViteConfig } from './vite.shared.mjs'

const currentFile = fileURLToPath(import.meta.url)
const currentDir = path.dirname(currentFile)

export default defineConfig(createHazelGymViteConfig(currentDir))
