
      - name: Deploy backend
        run: |
          rsync -az \
            --exclude='.env' \
            --exclude='data/' \
            --exclude='vendor/' \
            -e "ssh -p ${{ secrets.SERVER_PORT }} -o StrictHostKeyChecking=yes" \
            api/ \
            ${{ secrets.SERVER_USER }}@${{ secrets.SERVER_HOST }}:/var/www/html/fp/api/

      - name: Install PHP dependencies
        run: |
          ssh -p "${{ secrets.SERVER_PORT }}" \
            -o StrictHostKeyChecking=yes \
            "${{ secrets.SERVER_USER }}@${{ secrets.SERVER_HOST }}" \
            "cd /var/www/html/kokosek/api && composer install --no-dev --optimize-autoloader"