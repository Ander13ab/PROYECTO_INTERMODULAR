import path from 'node:path'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

function figmaAssetResolver(rootDir) {
  return {
    name: 'figma-asset-resolver',
    resolveId(id) {
      if (id.startsWith('figma:asset/')) {
        const filename = id.replace('figma:asset/', '')
        return path.resolve(rootDir, 'src/assets', filename)
      }

      return null
    },
  }
}

export function createHazelGymViteConfig(rootDir) {
  return {
    root: rootDir,
    plugins: [figmaAssetResolver(rootDir), react(), tailwindcss()],
    resolve: {
      alias: {
        '@': path.resolve(rootDir, 'src'),
      },
    },
    assetsInclude: ['**/*.svg', '**/*.csv'],
  }
}
