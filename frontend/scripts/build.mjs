import { build } from 'vite'
import { createHazelGymViteConfig } from '../vite.shared.mjs'

const rootDir = process.cwd()
const viteConfig = createHazelGymViteConfig(rootDir)

await build({
  ...viteConfig,
  configFile: false,
})
